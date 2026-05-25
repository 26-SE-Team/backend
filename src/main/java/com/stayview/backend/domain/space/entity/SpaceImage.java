package com.stayview.backend.domain.space.entity;

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
@Table(name = "space_images")
public class SpaceImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long imageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id", nullable = false)
	private Space space;

	@Column(name = "image_order", nullable = false)
	private int imageOrder;

	@Column(name = "image_url", nullable = false, length = 500)
	private String imageUrl;

	protected SpaceImage() {
	}

	public SpaceImage(Space space, int imageOrder, String imageUrl) {
		this.space = space;
		this.imageOrder = imageOrder;
		this.imageUrl = imageUrl;
	}

	public Long getImageId() {
		return imageId;
	}

	public int getImageOrder() {
		return imageOrder;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
