package com.makanstudios.roadalert.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Gcm {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@Column(length = 2047)
	public String regId;

	public Gcm() {
	}

	public Gcm(String regId) {
		this.regId = regId;
	}
}
