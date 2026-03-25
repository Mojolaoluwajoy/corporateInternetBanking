package org.app.corporateinternetbanking.userManagement;

import lombok.AllArgsConstructor;
import org.app.corporateinternetbanking.organizationManagement.Organization;
import org.app.corporateinternetbanking.organizationManagement.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organizationManagement.OrganizationRepository;
import org.app.corporateinternetbanking.userManagement.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.userManagement.dto.UserResponse;
import org.app.corporateinternetbanking.userManagement.exceptions.NotAnAdminException;
import org.app.corporateinternetbanking.userManagement.exceptions.UserAlreadyRegistered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.app.corporateinternetbanking.userManagement.Map.mapRequest;
import static org.app.corporateinternetbanking.userManagement.Map.mapResponse;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final OrganizationRepository organizationRepository;
    PasswordEncoder passwordEncoder;
    private final String adminRegKey="mojo12345";


    @Override
    public UserResponse register(UserRegistrationRequest request) throws UserAlreadyRegistered, NotAnAdminException, OrganizationDoesNotExist {
      if (!request.getAdminKey().equals(adminRegKey)){
          if (!"ADMIN".equalsIgnoreCase(request.getAdminKey())){
              throw new NotAnAdminException("Only admins can register");
          }
      }

        String nin=request.getNin();
                String email=request.getEmail();
                 if (repository.findByNin(nin).isPresent()|| repository.findByEmail(email).isPresent()){
                     throw new UserAlreadyRegistered("This user already exists");
        }
        Organization organization=organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(()-> new OrganizationDoesNotExist("This organization does not exist"));
                 User user=mapRequest(request);
                 user.setOrganization(organization);
     String encodedPassword=passwordEncoder.encode(request.getPassword());
   user.setPassword(encodedPassword);
User savedUser=repository.save(user);
return mapResponse(savedUser);
    }

    @Override
    public UserResponse createUser(UserRegistrationRequest request) throws UserAlreadyRegistered {

        String nin=request.getNin();
                String email=request.getEmail();
                 if (repository.findByNin(nin).isPresent()|| repository.findByEmail(email).isPresent()){
                     throw new UserAlreadyRegistered("This user already exists");
        }
                 User user=mapRequest(request);
     String encodedPassword=passwordEncoder.encode(request.getPassword());
   user.setPassword(encodedPassword);
User savedUser=repository.save(user);
return mapResponse(savedUser);
    }

    @Override
    public List<UserResponse> ViewAllUsers() {
        List<User> users=repository.findAll();
        List<UserResponse> userList=new ArrayList<>();
        for (User savedUser: users){
            UserResponse userResponse=new UserResponse();
            userResponse.setUserId(savedUser.getUserId());
            userResponse.setFirstName(savedUser.getFirstName());
            userResponse.setLastName(savedUser.getLastName());
            userResponse.setNin(savedUser.getNin());
            userResponse.setEmail(savedUser.getEmail());
            userResponse.setRole(savedUser.getRole());
            userResponse.setStatus(savedUser.getStatus());

        }
        return userList;
    }

  }
