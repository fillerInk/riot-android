/*
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.error

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.style.ForegroundColorSpan
import com.binaryfork.spanny.Spanny
import im.vector.R
import org.matrix.androidsdk.rest.model.MatrixError

private const val MAU = "monthly_active_user"

class ResourceLimitErrorFormatter(private val context: Context) {

    sealed class Mode(@StringRes val mauErrorRes: Int, @StringRes val defaultErrorRes: Int, @StringRes val contactRes: Int) {
        object Active : Mode(R.string.resource_limit_not_active_mau, R.string.resource_limit_not_active_default, R.string.resource_limit_not_active_contact)
        object NonActive : Mode(R.string.resource_limit_active_mau, R.string.resource_limit_active_default, R.string.resource_limit_active_contact)
    }

    fun format(mode: Mode, matrixError: MatrixError): CharSequence {
        val contactLink = contactAsLink(matrixError.adminContact)
        val error = if (MAU == matrixError.limitType) {
            context.getString(mode.mauErrorRes)
        } else {
            context.getString(mode.defaultErrorRes)
        }
        val contact = context.getString(mode.contactRes, contactLink)
        val contactHtml = Html.fromHtml(contact)
        return Spanny(error)
                .append(" ")
                .append(contactHtml)
    }

    private fun contactAsLink(email: String): String {
        val contactStr = context.getString(R.string.resource_limit_contact_admin)
        return "<a href=\"mailto:$email\">$contactStr</a>"
    }


}