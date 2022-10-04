package io.blindnet.identity
package services

import endpoints.objects.*
import errors.*

import cats.effect.*

import java.util.UUID

class ApplicationService(repos: Repositories) {
  def get(id: UUID): IO[ApplicationInfoPayload] =
    repos.applications.findById(id).orNotFound
      .map(ApplicationInfoPayload.apply)
}
