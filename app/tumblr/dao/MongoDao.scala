package tumblr.dao

import scala.concurrent.Future
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.Logger
import play.api.Play.current
import play.api.libs.json._

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson._
import reactivemongo.core.commands.Count

/**
 * Generic Mongo Dao trait.
 *
 * To use this class, import in your dao object:
 * <pre>
 *   <code>
 *    import scala.concurrent.ExecutionContext
 *    import ExecutionContext.Implicits.global
 *  </code>
 *  </pre>
 *
 * @tparam T the type to persist.
 * @tparam PK the PK type of the type T.
 *
 */
trait MongoDao[T, PK] {

  def db = ReactiveMongoPlugin.db
  def collection: JSONCollection = db.collection(collectionName())
  def collectionName(): String

  /**
   * Find all documents from the collection.
   *
   * @return a Future list of documents.
   */
  def findAll()(implicit reader: Reads[T]): Future[List[T]]= {
    Logger.debug(s"$collectionName - findAll()")
    collection
      .find(Json.obj())
      .cursor[T]
      .toList
  }

  /**
   * Find a document by its primary key in the collection.
   *
   * @param pk the PK
   * @param writer the implicit writer for the PK
   * @param reader the implicit reader for the object of type T
   * @return the document matching the PK.
   */
  def findByPK(pk: PK)(implicit writer: Writes[PK], reader: Reads[T]): Future[Option[T]] = {
    Logger.debug(s"$collectionName - findByPK($pk)")

    collection.find(Json.obj("_id" -> pk)).one[T]
  }

  /**
   * Saves an element into the collection.
   *
   * @param element the element to persist.
   */
  def save(element: T)(implicit writer: Writes[T]) = {
    Logger.debug(s"$collectionName - save($element)")
    collection.save(element)
  }

  def remove(pk: PK)(implicit writer: Writes[PK]) = {
    Logger.debug(s"$collectionName - remove(pk) ")
    collection.remove(pk)
  }

  /**
   * Remove all documents from the collection.
   */
  def removeAll() = {
    Logger.debug(s"$collectionName - removeAll()")
    collection.remove(Json.obj())
  }

  /**
   * Count the number of documents from the collection.
   */
  def count(): Future[Int] = {
    Logger.debug(s"$collectionName - count()")
    db.command(Count(collectionName))
  }

}