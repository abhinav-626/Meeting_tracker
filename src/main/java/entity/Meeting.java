package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meetings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "meeting_date", nullable = false)
    private Instant meetingDate;

    @Column(name = "raw_transcript", nullable = false, columnDefinition = "TEXT")
    private String rawTranscript;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingSource source = MeetingSource.MANUAL_PASTE;

    // Groups recurring meetings (e.g. weekly standup) so carry-forward logic
    // knows which prior meeting's incomplete items belong to this series.
    @Column(name = "series_id")
    private UUID seriesId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionItem> actionItems;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public enum MeetingSource {
        MANUAL_PASTE,
        FILE_UPLOAD
    }
}
