package com.platform.service;

import com.platform.dto.auth.WhoAmIResponseDTO;
import com.platform.dto.user.UserRequestDTO;
import com.platform.dto.user.UserResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.Employee;
import com.platform.entity.ProvidedService;
import com.platform.entity.User;
import com.platform.exception.UserNotFoundException;
import com.platform.repository.BusinessRepository;
import com.platform.repository.EmployeeRepository;
import com.platform.repository.ServiceRepository;
import com.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponseDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public UserResponseDTO getUserDTOById(UUID id) {
        return userRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = userRepository.save(user);
        return toResponseDTO(user);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public WhoAmIResponseDTO whoami() {
        User user = getUser();

        List<Business> businessList = businessRepository.findByOwnerId(user.getId());

        List<ProvidedService> servicesList = new ArrayList<>();
        List<Employee> employeeList = new ArrayList<>();
        if (!businessList.isEmpty()) {
            Business firstBusiness = businessList.get(0);
            servicesList = serviceRepository.findByBusinessId(firstBusiness.getId());
            employeeList = employeeRepository.findByBusinessId(firstBusiness.getId());
        }

        return WhoAmIResponseDTO.builder()
                .user(user)
                .employeeList(employeeList)
                .businessList(businessList)
                .providedServiceList(servicesList)
                .build();
    }


    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        return getUserByUsername(username);
    }

    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
