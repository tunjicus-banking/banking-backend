package com.tunjicus.bank.jobPostings;

import com.tunjicus.bank.jobPostings.dtos.PostJobPostingDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "job_postings")
@Getter
@Setter
@NoArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_posting_id")
    private int id;

    @Column
    private int positionId;

    @Column
    @Nationalized
    private String description;

    @Column
    private Date upSince;

    @Column
    private boolean active;

    public JobPosting(PostJobPostingDto dto) {
        positionId = dto.getPositionId();
        description = dto.getDescription();
    }
}
