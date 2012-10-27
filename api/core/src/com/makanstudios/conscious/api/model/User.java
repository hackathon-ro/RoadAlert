package com.makanstudios.conscious.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class User {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@Column(length = 255)
	public String name;

	@Column(length = 255)
	public String email;

	public User() {
	}

	@Override
	public String toString() {
		return "[" + id + "] " + name + " " + email;
	}
}
