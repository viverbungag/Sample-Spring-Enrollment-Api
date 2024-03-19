package com.skipp.enlistment.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 *  Represents a time slot when a class will be held.
 *  <p>
 *  The format would be: [MTH|TF|WS] HH:mm-HH:mm
 *  <p>
 *  Example: TF 08:30-11:30
 */
public class Schedule {

    public static final Schedule TBA = new Schedule();

    /**
     * Days when the class will be held
     */
    private final Days days;

    /**
     * Class starting time
     */
    private final LocalTime start;

    /**
     * Class ending time
     */
    private final LocalTime end;

    private Schedule() {
        days = null;
        start = null;
        end = null;
    }

    public Schedule(Days days, LocalTime start, LocalTime end) {
        notNull(days);
        notNull(start);
        notNull(end);
        this.days = days;
        this.start = start;
        this.end = end;
    }

    /**
     * Creates a Schedule object from a String.
     * <p>
     * The string should follow the format: [MTH|TF|WS] HH:mm-HH:mm
     *
     * @param scheduleString   the string to parse
     * @return                 the equivalent Schedule object
     * @throws IllegalArgumentException if the format was not followed
     */
    public static Schedule valueOf(String scheduleString) {
        if (StringUtils.trimToEmpty(scheduleString).equals("")
                || StringUtils.trimToEmpty(scheduleString)
                .equalsIgnoreCase("TBA")
                || StringUtils.trimToEmpty(scheduleString).equals("NULL")) {
            return TBA;
        } else {
            String[] tokens = scheduleString.split(" |-");
            try {
                return new Schedule(Days.valueOf(tokens[0]),
                        LocalTime.parse(tokens[1]), LocalTime.parse(tokens[2]));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid format for schedule. Should be: [MTH|TF|WS] HH:mm-HH:mm");
            }
        }

    }

    /**
     * Checks if this Schedule is in conflict with another.
     *
     * @param other the other Schedule to compare against
     * @throws ScheduleConflictException if there is conflict
     */
    public void notOverlappingWith(Schedule other) {
        if (isOverlappingWith(other)) {
            throw new ScheduleConflictException("Schedule Conflict: -> " +
                    "This schedule: " + this + "; Other schedule: " + other);
        }
    }

    /**
     * Checks if this Schedule overlaps with another Schedule.
     *
     * @param other the other Schedule to compare with
     * @return      true if there is an overlap between the two Schedule objects
     */
    public boolean isOverlappingWith(Schedule other) {
        if (this == TBA || other == TBA) {
            return false;
        }
        return (this.days == other.days) && (this.start.isBefore(other.end)
                && this.end.isAfter(other.start));
    }

    @Override
    public String toString() {
        return this == TBA ? "TBA" : days + " " + start + "-" + end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Schedule other))
            return false;
        if (days != other.days)
            return false;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (start == null) {
            return other.start == null;
        } else return start.equals(other.start);
    }

}

