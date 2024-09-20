package com.tipsuy.readaufplayers.dao;

import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SequenceRepository {

   private final MongoTemplate mongoTemplate;

   public Long getNextSequenceValue(String sequenceName) {
      // Definir la consulta para buscar el documento por su _id
      final var query = new Query(Criteria.where("_id").is(sequenceName));

      // Definir la actualización para incrementar en 1 el valor de 'seq'
      final var update = new Update().inc("seq", 1);

      // Ejecutar findAndModify para incrementar y devolver el nuevo valor
      final var counter = mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true).upsert(true), // Retorna el nuevo valor y hace un insert si no existe
            Document.class,
            "counters"  // La colección de secuencias
      );

      // Devolver el nuevo valor de la secuencia
      return counter != null ? counter.getLong("seq") : 1L;
   }

}
