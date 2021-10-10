package com.ihis.ewallet.security;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.repo.EwalletUserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	EwalletUserRepository eWalletUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<EWalletUser> eWalletUser = eWalletUserRepository.findByEmail(username);
		
		if (eWalletUser.isPresent()) {
			return new User(username, "",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}