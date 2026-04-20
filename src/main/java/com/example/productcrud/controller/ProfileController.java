package com.example.productcrud.controller;

import com.example.productcrud.dto.ChangePasswordRequest;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    //Ronald Saut M. buat file ProfileController di folder Controller
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // 1. View profile
    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", getCurrentUser(userDetails));
        return "profile/view";
    }

    // 2. Tampilkan form edit profile
    @GetMapping("/edit")
    public String showEditProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", getCurrentUser(userDetails));
        return "profile/edit";
    }

    // 3. Proses simpan edit profile
    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute User updatedUser, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(userDetails);

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAddress(updatedUser.getAddress());
        user.setBio(updatedUser.getBio());
        user.setProfileImageUrl(updatedUser.getProfileImageUrl());

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui :)");
        return "redirect:/profile";
    }

    // 4. Tampilkan form change password
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordRequest", new ChangePasswordRequest());
        return "profile/change-password";
    }

    // 5. Proses change password
    @PostMapping("/change-password")
    public String processChangePassword(@ModelAttribute ChangePasswordRequest request, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(userDetails);

        // Validasi dimana password lama sesuai dengan di database
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password lama gak sesuai!");
            return "redirect:/profile/change-password";
        }

        // Validasi dimana password baru dan konfirmasi password sama
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password baru dan konfirmasi gak cocok!");
            return "redirect:/profile/change-password";
        }

        // Enkripsi dan simpan password baru
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Password berhasil diubah!");
        return "redirect:/profile";
    }
}
