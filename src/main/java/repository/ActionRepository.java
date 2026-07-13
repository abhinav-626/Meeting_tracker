package repository;


import entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<ActionItem, UUID> {

    List<ActionItem> findByMeetingId(UUID meetingId);

    List<ActionItem> findByOwnerId(UUID ownerId);

    // Used by the Day 6 scheduled job to find incomplete items eligible for carry-forward
    List<ActionItem> findByMeetingIdAndStatusIn(UUID meetingId, List<ActionItem.ActionItemStatus> statuses);
}
