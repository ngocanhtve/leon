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

package com.svenjacobs.app.leon.service

import com.svenjacobs.app.leon.repository.CleanerRepositoryFake
import com.svenjacobs.app.leon.services.CleanerService
import com.svenjacobs.app.leon.test.MOCK_SANITIZER_STRATEGY_EXECUTOR
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class CleanerServiceTest : ShouldSpec({

    val service = CleanerService(
        repository = CleanerRepositoryFake(),
        sanitizerStrategyExecutor = MOCK_SANITIZER_STRATEGY_EXECUTOR,
    )

    context("clean()") {

        should("remove tracking parameters") {
            val result =
                service.clean("https://www.some.site/?utm_source=source&utm_medium=medium&ga_source=ga_source&ga_medium=ga_medium")

            result.isSuccess shouldBe true
            val cleaned = result.getOrThrow()

            cleaned.cleanedText should be("https://www.some.site/")
            cleaned.cleanedParametersCount should be(4)
            cleaned.urls should haveSize(1)
            cleaned.urls.first() should be("https://www.some.site/")
        }

        should("keep non-tracking parameters") {
            val result =
                service.clean("https://www.some.site/?utm_source=source&utm_medium=medium&page=1&q=query")

            result.isSuccess shouldBe true
            val cleaned = result.getOrThrow()

            cleaned.cleanedText should be("https://www.some.site/?page=1&q=query")
            cleaned.cleanedParametersCount should be(2)
            cleaned.urls should haveSize(1)
            cleaned.urls.first() should be("https://www.some.site/?page=1&q=query")
        }

        should("keep anchor tags after cleaned parameters") {
            val result =
                service.clean("https://www.some.site/?utm_source=source&utm_medium=medium#anchor")

            result.isSuccess shouldBe true
            val cleaned = result.getOrThrow()

            cleaned.cleanedText should be("https://www.some.site/#anchor")
            cleaned.urls.first() should be("https://www.some.site/#anchor")
        }

        should("clean multiple URLs in text") {
            val result =
                service.clean("Hey, I would like to share this https://www.some.site/?utm_source=source&utm_medium=medium link as well as this https://www.some2.site?wt_mc=wt_mc link :)")

            result.isSuccess shouldBe true
            val cleaned = result.getOrThrow()

            cleaned.cleanedText should be("Hey, I would like to share this https://www.some.site/ link as well as this https://www.some2.site link :)")
            cleaned.cleanedParametersCount should be(3)
            cleaned.urls should haveSize(2)
            cleaned.urls[0] should be("https://www.some.site/")
            cleaned.urls[1] should be("https://www.some2.site")
        }

        should("clean Amazon URLs") {
            val result =
                service.clean("https://smile.amazon.de/Goodstoworld-Herren-T-Shirt-Gr-Shark/dp/B07Q5KYJ9G/ref=sr_1_22?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&dchild=1&keywords=Nyan+cat&qid=1623259976&sr=8-22")

            result.isSuccess shouldBe true
            val cleaned = result.getOrThrow()

            cleaned.cleanedText should be("https://smile.amazon.de/Goodstoworld-Herren-T-Shirt-Gr-Shark/dp/B07Q5KYJ9G/")
            cleaned.cleanedParametersCount should be(6)
        }
    }
})
