package netSec.dal;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import netSec.data.UserEntity;

public interface Crud extends ListCrudRepository<UserEntity, String> {
	public UserEntity findByUserName(@Param("userName") String userName);

	public UserEntity findByEmail(@Param("email") String email);

}
