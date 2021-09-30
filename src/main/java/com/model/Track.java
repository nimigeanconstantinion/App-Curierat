package com.model;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
@ToString
public class Track implements Comparable<Track> {
    private int id_colet;
    private String awb_colet;
    private LocalDateTime data_status;
    private String status;
    private String locatie_id;

    public Track(int id_colet, String awb_colet){
        this.id_colet=id_colet;
        this.awb_colet=awb_colet;
        this.data_status=LocalDateTime.now();
        this.locatie_id="";
        this.status="";

    }

    @Override
    public boolean equals(Object o) {
        Track t=(Track) o;
        return this.id_colet==t.id_colet&&this.awb_colet.equals(t.getAwb_colet())&&this.locatie_id.equals(t.getLocatie_id());
    }

    @Override
    public int compareTo(Track t) {
        if (awb_colet.compareTo(t.awb_colet) > 0) {
            return 1;
        } else if (awb_colet.compareTo(t.awb_colet) < 0) {
            return -1;
        } else {
            if (data_status.compareTo(t.data_status) > 0) {
                return 1;
            } else if (data_status.compareTo(t.data_status) < 0) {
                return -1;
            } else {
                return 0;
            }

        }
    }

}
