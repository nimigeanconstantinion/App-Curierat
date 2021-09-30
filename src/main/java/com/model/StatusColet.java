package com.model;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class StatusColet implements Comparable<StatusColet> {
    private int id_colet;
    private String awb_colet;
    private LocalDateTime data_status;
    private String status;
    private String locatie_id;

    @Override
    public boolean equals(Object o) {
        StatusColet s = (StatusColet) o;
        return s.getAwb_colet().equals(this.awb_colet) && s.getData_status() == this.data_status;
    }

    @Override
    public int compareTo(StatusColet s) {
        return this.data_status.compareTo(s.data_status);
    }
}
