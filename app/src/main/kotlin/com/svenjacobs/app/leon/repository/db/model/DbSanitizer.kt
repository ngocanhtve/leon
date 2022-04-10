/*
 * Léon - The URL Cleaner
 * Copyright (C) 2021 Sven Jacobs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.svenjacobs.app.leon.repository.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Database entity of a sanitizer
 *
 * @see DbSanitizerConfig
 * @see DbSanitizerView
 */
@Entity(
    tableName = "sanitizers",
    indices = [Index(value = ["name"], unique = true)],
)
data class DbSanitizer(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val type: Type,
    val name: String,
    val data: Map<String, String?> = emptyMap(),
    val description: String? = null,
    val isDefault: Boolean = false,
) {
    enum class Type { QUERY_PARAMETER, REGEX }
}