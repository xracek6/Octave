/*
 * MIT License
 *
 * Copyright (c) 2020 Melms Media LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.octave.bot.commands.music.dj

import gg.octave.bot.commands.music.PLAY_MESSAGE
import gg.octave.bot.entities.framework.DJ
import gg.octave.bot.entities.framework.MusicCog
import gg.octave.bot.utils.extensions.manager
import gg.octave.bot.utils.extensions.voiceChannel
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command

class Resume : MusicCog {
    override fun requireVoiceState() = true

    @DJ
    @Command(aliases = ["rq"], description = "Resume the music queue.")
    fun resumequeue(ctx: Context) {
        val manager = ctx.manager
        val connectedChannel = ctx.guild!!.audioManager.connectedChannel

        if (connectedChannel != null && ctx.voiceChannel != connectedChannel) {
            return ctx.send("You need to be in my voice channel.")
        }

        if (manager.queue.isEmpty()) {
            return ctx.send("The queue is empty.\n${PLAY_MESSAGE.format(ctx.trigger)}")
        }

        if (manager.lastTrack != null) {
            return ctx.send("There's nothing to resume as the player has been active here!")
        }

        //Reset expire time if play has been called.
        manager.queue.clearExpire()

        //Poll next from queue and force that track to play.
        manager.openAudioConnection(ctx.voiceChannel!!, ctx)
        manager.nextTrack()

        ctx.send("Queue has been resumed.")
    }
}