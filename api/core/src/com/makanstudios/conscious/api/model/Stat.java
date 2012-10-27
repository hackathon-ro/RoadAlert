package com.makanstudios.conscious.api.model;

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
public class Stat {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@JoinColumn
	@ManyToOne
	public User user;

	@JoinColumn
	@ManyToOne
	public History history;

	@Column
	public boolean accepted;

	@Column
	public boolean finished;

	public Stat() {
	}

	@Override
	public String toString() {
		return "[" + id + "] " + accepted + " [" + finished + "]";
	}
}
