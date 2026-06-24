package com.stayview.backend.domain.space.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "three_d_models")
public class ThreeDModel extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "model_id")
	private Long modelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id", nullable = false)
	private Space space;

	@Column(name = "model_url", nullable = false, length = 500)
	private String modelUrl;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	protected ThreeDModel() {
	}
}
