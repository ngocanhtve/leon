/*
 * Léon - The URL Cleaner
 * Copyright (C) 2022 Sven Jacobs
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

package com.svenjacobs.app.leon.feature.sanitizer.google.search

import com.svenjacobs.app.leon.core.domain.sanitizer.Sanitizer
import java.net.URLDecoder
import javax.inject.Inject

class GoogleSearchSanitizer @Inject constructor() : Sanitizer {

    override fun invoke(input: String): String {
        val result = URL_PARAMETER_REGEX.find(input) ?: return input
        val group = result.groups[1] ?: return input
        return URLDecoder.decode(group.value, "UTF-8")
    }

    private companion object {
        private val URL_PARAMETER_REGEX = Regex("[?&]url=([^&]+)")
    }
}
