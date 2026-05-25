package com.stayview.backend.domain.space.entity;

import com.stayview.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class SpaceReport extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long reportId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id", nullable = false, unique = true)
	private Space space;

	@Column(name = "summary", columnDefinition = "text")
	private String summary;

	protected SpaceReport() {
	}

	private SpaceReport(Space space, String summary) {
		this.space = space;
		this.summary = summary;
	}

	public static SpaceReport registrationReport(Space space) {
		return new SpaceReport(space, "Registration report generated when the space was created.");
	}

	public Long getReportId() {
		return reportId;
	}

	public Space getSpace() {
		return space;
	}

	public String getSummary() {
		return summary;
	}
}
