package wifi4eu.wifi4eu.service.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.UserDTO;
import wifi4eu.wifi4eu.mapper.user.UserMapper;
import wifi4eu.wifi4eu.repository.user.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userMapper.toDTOList(Lists.newArrayList(userRepository.findAll()));
    }

    public UserDTO getUserById(int userId) {
        return userMapper.toDTO(userRepository.findOne(userId));
    }

    public UserDTO createUser(UserDTO userDTO) {
        return userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));
    }

    public UserDTO deleteUser(int userId) {
        UserDTO userDTO = userMapper.toDTO(userRepository.findOne(userId));
        if (userDTO != null) {
            userRepository.delete(userMapper.toEntity(userDTO));
            return userDTO;
        } else {
            return null;
        }
    }
}