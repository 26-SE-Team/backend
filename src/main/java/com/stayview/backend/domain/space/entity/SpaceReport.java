package com.stayview.backend.domain.space.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
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

	public static SpaceReport registrationReport(Space space) {
		return SpaceReport.builder()
			.space(space)
			.summary("등록 신고")
			.build();
	}
}
