package com.tipsuy.readaufplayers.config;

import static com.tipsuy.readaufplayers.util.DateUtil.DATETIME_FORMAT;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {

  @Override
  public OffsetDateTime decode(final BsonReader reader, final DecoderContext decoderContext) {
    return OffsetDateTime.parse(reader.readString(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
  }

  @Override
  public void encode(final BsonWriter writer, final OffsetDateTime value, final EncoderContext encoderContext) {
    writer.writeString(value.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
  }

  @Override
  public Class<OffsetDateTime> getEncoderClass() {
    return OffsetDateTime.class;
  }
}
