package com.atronandbeyond;

import java.util.List;

class Command{
    enum Cmd{
        IMAGE, SONG, IMAGE_DESATURATE,
        ABSTRACT_BACKGROUND, ABSTRACT_BACKGROUND_MIXER_BLEND_OVERLAY,
        ABSTRACT_BACKGROUND_MIXER_HALF_OPACITY, CITY_STATE_TITLE,
        CITY_STATE_STOP,
    }
    static String getCommand(Cmd command, List<String> args){
        String result = "";
        switch (command) {
            case SONG:
                result = "play 1-1 " + args.get(0);
                break;
            case IMAGE:
                result = "play 1-2 " + args.get(0) + " mix 100 easeinsine";
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
            case ABSTRACT_BACKGROUND_MIXER_HALF_OPACITY:
                result = "mixer 1-3 opacity .5";
                break;
            case CITY_STATE_TITLE:
                result = "cg 1 add 2 CITY_STATE_TITLE/INDEX 1 \"{\\\"city\\\":\\\"" + args.get(0) + "\\\",\\\"state\\\":\\\"" + args.get(1) + "\\\"}\"";
                break;
            case CITY_STATE_STOP:
                result = "cg 1 stop 2";
                break;
        }
        return result;
    }
}