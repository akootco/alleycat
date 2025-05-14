package co.akoot.plugins.alleycat.extensions

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.extensions.Key.CAN_ATTACK
import co.akoot.plugins.alleycat.extensions.Key.CAN_BREAK_BLOCKS
import co.akoot.plugins.alleycat.extensions.Key.CAN_PICK_UP_ITEMS
import co.akoot.plugins.alleycat.extensions.Key.CAN_PLACE_BLOCKS
import co.akoot.plugins.alleycat.extensions.Key.IS_MUTED
import co.akoot.plugins.alleycat.extensions.Key.IS_SILENT_ADVANCEMENTS
import co.akoot.plugins.alleycat.extensions.Key.IS_SILENT_DEATH
import co.akoot.plugins.alleycat.extensions.Key.IS_SILENT_JOIN
import co.akoot.plugins.alleycat.extensions.Key.IS_SILENT_LEAVE
import co.akoot.plugins.alleycat.extensions.Key.RESTRAINING_ORDER_TARGETS
import co.akoot.plugins.bluefox.extensions.getPDC
import co.akoot.plugins.bluefox.extensions.getPDCList
import co.akoot.plugins.bluefox.extensions.setPDC
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

object Key {
    const val CAN_PICK_UP_ITEMS = "can_pickup_items"
    const val IS_MUTED = "is_muted"
    const val CAN_BREAK_BLOCKS = "can_break_blocks"
    const val CAN_PLACE_BLOCKS = "can_place_blocks"
    const val CAN_ATTACK = "can_attack"
    const val IS_SILENT_JOIN = "is_silent_join"
    const val IS_SILENT_LEAVE = "is_silent_leave"
    const val IS_SILENT_DEATH = "is_silent_death"
    const val IS_SILENT_ADVANCEMENTS = "is_silent_advancements"
    const val RESTRAINING_ORDER_TARGETS = "restraining_order_targets"
}

var Player.canPickUpItems: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(CAN_PICK_UP_ITEMS)) ?: true
    set(value) = setPDC(AlleyCat.key(CAN_PICK_UP_ITEMS), value)

var Player.isMuted: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(IS_MUTED)) ?: false
    set(value) = setPDC(AlleyCat.key(IS_MUTED), value)

var Player.canBreakBlocks: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(CAN_BREAK_BLOCKS)) ?: true
    set(value) = setPDC(AlleyCat.key(CAN_BREAK_BLOCKS), value)

var Player.canPlaceBlocks: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(CAN_PLACE_BLOCKS)) ?: true
    set(value) = setPDC(AlleyCat.key(CAN_PLACE_BLOCKS), value)

var Player.canAttack: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(CAN_ATTACK)) ?: true
    set(value) = setPDC(AlleyCat.key(CAN_ATTACK), value)

var Player.isSilentJoin: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(IS_SILENT_JOIN)) ?: false
    set(value) = setPDC(AlleyCat.key(IS_SILENT_JOIN), value)

var Player.isSilentLeave: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(IS_SILENT_LEAVE)) ?: false
    set(value) = setPDC(AlleyCat.key(IS_SILENT_LEAVE), value)

var Player.isSilentDeath: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(IS_SILENT_DEATH)) ?: false
    set(value) = setPDC(AlleyCat.key(IS_SILENT_DEATH), value)

var Player.isSilentAdvancements: Boolean
    get() = getPDC<Boolean>(AlleyCat.key(IS_SILENT_ADVANCEMENTS)) ?: false
    set(value) = setPDC(AlleyCat.key(IS_SILENT_ADVANCEMENTS), value)

val Player.restrainingOrderTargets: List<Player>
    get() = getPDCList<Player>(AlleyCat.key(RESTRAINING_ORDER_TARGETS)) ?: listOf()

var Player.isIncapacitated: Boolean
    get() = !(canBreakBlocks && canPickUpItems && canAttack && canPlaceBlocks)
    set(value) {
        canBreakBlocks = !value
        canPickUpItems = !value
        canAttack = !value
        canPlaceBlocks = !value
    }
