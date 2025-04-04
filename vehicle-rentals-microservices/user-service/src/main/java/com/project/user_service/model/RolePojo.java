package com.project.user_service.model;

import java.util.List;


import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data

public class RolePojo {
	private long roleId;
	private String name;

	private List<UserCredentialPojo> usersCrentials;

}
