package org.formation.domain;

import java.util.List;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Livreur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String nom;
	
	private String telephone;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Review> reviews;

	@Embedded
	private Position position;
}
