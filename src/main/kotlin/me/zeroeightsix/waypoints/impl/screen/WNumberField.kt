package me.zeroeightsix.waypoints.impl.screen

import spinnery.widget.WTextField

class WNumberField : WTextField() {

    override fun onCharTyped(character: Char, keyCode: Int) {
        if (!character.isDigit() && !(cursor == Cursor(0, 0) && character == '-')) return
        super.onCharTyped(character, keyCode)
    }

}
