package com.tipsuy.readaufplayers.util;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  public static final DateTimeFormatter MATCH_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");

  public String instantToStringDateTime(@NonNull final Instant instant, final Clock clock) {
    final var zonedDateTime = instant.atZone(clock.getZone());
    final var formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    return formatter.format(zonedDateTime);
  }

  public String instantToStringDate(@NonNull final Instant instant, final Clock clock) {
    final var zonedDateTime = instant.atZone(clock.getZone());
    final var formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    return formatter.format(zonedDateTime);
  }

  public Instant stringDateTimeToInstant(@NonNull final String dateTime, final Clock clock) {
    final var formattedDateTime = STR."\{dateTime.replace(" ", "T").replace("/","-")}:00Z";
    final var zoneId = clock.getZone();
    final var zonedDateTime = ZonedDateTime.parse(formattedDateTime).withZoneSameInstant(zoneId);
    return zonedDateTime.toInstant();
  }

  public Instant stringDateToInstant(@NonNull final String date, final Clock clock) {
    final var zoneId = clock.getZone();
    final var zonedDateTime = ZonedDateTime.parse(STR."\{date}T12:00:00Z".trim().replace("/","-")).withZoneSameInstant(zoneId);
    return zonedDateTime.toInstant();
  }

  public Instant stringDateToInstant(@NonNull final String date) {
    return stringDateToInstant(date, Clock.systemDefaultZone());
  }

  public OffsetDateTime stringDateToOffsetDateTime(@NonNull final String date) {
    return OffsetDateTime.parse(STR."\{date.trim()}T15:00:00Z").atZoneSameInstant(ZoneId.of("-03:00")).toOffsetDateTime();
  }

  public OffsetDateTime stringDateToOffsetDateTime(@NonNull final String date, final Clock clock) {
    return OffsetDateTime.parse(STR."\{date.trim()}T15:00:00Z").atZoneSameInstant(clock.getZone()).toOffsetDateTime();
  }

  public String offsetDateTimeToStringDateTime(@NonNull final OffsetDateTime dateTime, final Clock clock) {
    final var formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    return formatter.format(dateTime.atZoneSameInstant(clock.getZone()));
  }

  public String offsetDateTimeToStringDate(@NonNull final OffsetDateTime date, final Clock clock) {
    final var formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    return formatter.format(date.atZoneSameInstant(clock.getZone()));
  }

  public OffsetDateTime toOffsetDateTime(final int date, final int time) {
    String dateStr = String.valueOf(date);
    String timeStr = StringUtils.leftPad(String.valueOf(time), 6, '0');
    return OffsetDateTime.parse(dateStr + ' ' + timeStr, MATCH_DATETIME_FORMATTER);
  }

  public static void main(String[] args) {
    System.out.println(stringDateToOffsetDateTime("2017-06-26"));
    System.out.println(stringDateToOffsetDateTime("2011-01-13", Clock.systemDefaultZone()));
    ZoneOffset zoneOffset = Clock.systemDefaultZone().getZone().getRules().getOffset(Instant.now(Clock.systemDefaultZone()));
    System.out.println(offsetDateTimeToStringDateTime(OffsetDateTime.of(2001,9,26,17,15,23,0, zoneOffset), Clock.systemDefaultZone()));
    System.out.println(offsetDateTimeToStringDate(OffsetDateTime.of(1987,12,19,0,0,0,0, zoneOffset), Clock.systemDefaultZone()));
  }

}
