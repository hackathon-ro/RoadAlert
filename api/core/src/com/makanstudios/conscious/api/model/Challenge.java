package com.makanstudios.conscious.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Challenge {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@Column(length = 255)
	public String title;

	@Column(length = 1000)
	public String description;

	public Challenge() {
	}

	@Override
	public String toString() {
		return "[" + id + "] " + title + " [" + description + "]";
	}
}
