package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class FollowupHistoryResponse(
@SerialName("data")
var data: List<FollowupHistoryItem>
) : ResponseBase()
