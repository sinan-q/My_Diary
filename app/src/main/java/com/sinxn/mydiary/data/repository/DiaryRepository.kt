package com.sinxn.mydiary.data.repository

import androidx.sqlite.db.SupportSQLiteProgram
import androidx.sqlite.db.SupportSQLiteQuery
import com.sinxn.mydiary.data.local.dao.DiaryDao
import com.sinxn.mydiary.data.local.entities.Diary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val diaryDao: DiaryDao,
) {

    fun getAllDiaries(): Flow<List<Diary>> = diaryDao.getAllDiaries()

    suspend fun insertDiary(diary: Diary): Long = diaryDao.insertDiary(diary)


    suspend fun deleteDiary(diary: Diary): Int = diaryDao.deleteDiary(diary)

    suspend fun getDiaryByDate(date: LocalDate): Diary? = diaryDao.getDiaryByDate(date)

    suspend fun getDiaryById(id: Long): Diary? = diaryDao.getDiaryById(id)

    suspend fun searchDiaries(query: String): List<Diary> = diaryDao.searchDiaries(query)

    suspend fun searchDiariesWithTokens(tokens: List<String>, operator: String): List<Diary> {
        if (tokens.isEmpty()) return emptyList()

        // Define the columns you want to search in
        val searchColumns = listOf("title", "content")

        // Build the WHERE clause
        // For each token, we check if it's in EITHER the title OR the content.
        // These individual (title LIKE ? OR content LIKE ?) blocks are then joined by the main 'operator' (AND/OR).
        val whereClause = tokens.joinToString(separator = " $operator ") { _ ->
            // For each token, create a condition that searches in all specified columns
            val columnConditions = searchColumns.joinToString(separator = " OR ") { column ->
                "$column LIKE ?"
            }
            "($columnConditions)" // Parenthesize for correct precedence with the main operator
        }
        val queryString = "SELECT * FROM diary WHERE $whereClause ORDER BY date DESC"

        // Prepare arguments for the query
        // Each token will be used for each search column
        val args = tokens.flatMap { token ->
            searchColumns.map { "%$token%" }
        }.toTypedArray()

        val simpleSQLiteQuery = object : SupportSQLiteQuery {
            override val sql: String
                get() = queryString
            override val argCount: Int
                get() = args.size
            override fun bindTo(statement: SupportSQLiteProgram) {
                args.forEachIndexed { index, value ->
                    statement.bindString(index + 1, value)
                }
            }
        }
        return diaryDao.searchDiariesWithTokens(simpleSQLiteQuery)
    }
}