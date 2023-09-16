package com.example.racingcar.ui.models

import com.example.racingcar.R

data class NightRacingResourcePack(
    override val backgroundImageDrawable: Int = R.drawable.bg_road_night,
    override val carImageDrawable: Int = R.drawable.ic_car,
    override val blockerImageDrawable: Int = R.drawable.ic_block_night
) : RacingResourcePack