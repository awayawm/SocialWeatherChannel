package com.atronandbeyond;

import java.util.List;
import java.util.logging.Logger;

class Command{
    enum Cmd{
        IMAGE, SONG, IMAGE_DESATURATE,
        ABSTRACT_BACKGROUND, ABSTRACT_BACKGROUND_MIXER_BLEND_OVERLAY,
        ABSTRACT_BACKGROUND_MIXER_OPACITY, CITY_STATE_TITLE,
        CITY_STATE_STOP, MUSIC_TITLE, MUSIC_TITLE_STOP,
        FACT_TITLE, FACT_TITLE_STOP;
    }

    static Logger logger = Logger.getLogger("Command");
    static String getCommand(Cmd command, List<String> args){
        String result = "";
        switch (command) {
            case SONG:
                result = "play 1-1 " + args.get(0);
                break;
            case IMAGE:
                String song = args.get(0);
                result = "play 1-2 " + song.substring(0, song.lastIndexOf(".")) + " mix 100 easeinsine";
                break;
            case IMAGE_DESATURATE:
                result = "mixer 1-2 saturation .5";
                break;
            case ABSTRACT_BACKGROUND:
                result = "play 1-3 ABSTRACT_BACKGROUND";
                break;
            case ABSTRACT_BACKGROUND_MIXER_BLEND_OVERLAY:
                result = "mixer 1-3 blend overlay";
                break;
            case ABSTRACT_BACKGROUND_MIXER_OPACITY:
                result = "mixer 1-3 opacity .33";
                break;
            case CITY_STATE_TITLE:
                result = "cg 1 add 2 CITY_STATE_TITLE/INDEX 1 \"{\\\"city\\\":\\\"" + args.get(0) + "\\\",\\\"state\\\":\\\"" + args.get(1) + "\\\"}\"";
                break;
            case CITY_STATE_STOP:
                result = "cg 1 stop 2";
                break;
            case MUSIC_TITLE:
                result = "cg 1 add 3 MUSIC_TITLE/INDEX 1 \"{\\\"album\\\":\\\"" + args.get(1) + "\\\",\\\"artist\\\":\\\"" + args.get(0) + "\\\",\\\"song\\\":\\\"" + args.get(1) + "\\\"}\"";
                break;
            case MUSIC_TITLE_STOP:
                result = "cg 1 stop 3";
                break;
            case FACT_TITLE:
                result = "cg 1 add 3 FACT_TITLE/INDEX 1 \"{\\\"heading\\\":\\\"" + args.get(0) + "\\\",\\\"fact\\\":\\\"" + args.get(1) + "\\\"}\"";
//                logger.info("fact command: " + result);
                break;
            case FACT_TITLE_STOP:
                result = "cg 1 stop 3";
                break;
        }
        return result;
    }
}