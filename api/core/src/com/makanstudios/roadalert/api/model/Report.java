package com.makanstudios.roadalert.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Report {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@JoinColumn
	@ManyToOne
	public History history;

	@JoinColumn
	@ManyToOne
	public Challenge challenge;

	@JoinColumn
	@ManyToOne
	public User user;

	@Column
	public int timesChallenge;

	@Column
	public int ratingConscious;

	@Column(length = 2000)
	public String text;

	public Report() {
	}

}
