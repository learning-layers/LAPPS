package de.rwth.dbis.layers.lapps;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;

public class Utils {
  public static final String DELETE_USERS_QUERY = "delete UserEntity";
  public static final String DELETE_APPS_QUERY = "delete AppEntity";

  /**
   * Generates a random integer in a range (min-max)
   * 
   * @param min Minimal range value
   * @param max Maximal range value
   * @return an int in the range (min-max)
   */
  public static int generateRandomInt(int min, int max) {
    return (int) (min + Math.random() * (max - min - 1));
  }

  public static String generateRandomString() {
    final Random random = new Random();
    return new BigInteger(80, random).toString();
  }

  // @deprecated
  public static AppEntity createDummyAppWithInstance(AppPlatformEntity onPlatform) {
    AppInstanceEntity instance = new AppInstanceEntity(onPlatform, "dummy app instance");
    AppEntity app = new AppEntity("dummy app");
    app.addInstance(instance);
    return app;
  }

  public static <E> E getRandomEntity(List<E> list) {
    return list.get(generateRandomInt(0, list.size()));
  }

  public static <E> E removeRandomEntity(List<E> list) {
    return list.remove(generateRandomInt(0, list.size()));
  }

  /*
   * @Test public void mockupAppGenerator() { for (int i = 0; i < 1; i++) { //
   * System.out.println(MockupAppGenerator.getRandomName()); //
   * System.out.println(MockupAppGenerator.getRandomIpsumBlock()); //
   * System.out.println(MockupAppGenerator.getRandomText(700, 3)); //
   * System.out.println(MockupAppGenerator.getRandomImageUrl());
   * 
   * } }
   */

  /**
   * Generates mockup app instances
   *
   */
  public static class MockupAppGenerator {
    private final static String[] names = new String[] {"Sun", "Jupiter", "Saturn", "Uranus",
        "Neptune", "Earth", "Venus", "Mars", "Ganymede", "Titan", "Mercury", "Callisto", "Io",
        "Moon", "Europa", "Triton", "Pluto", "Eris", "Titania", "Rhea", "Oberon", "Iapetus",
        "Makemake", "Haumea", "Charon", "Umbriel", "Ariel", "Dione", "Quaoar", "Tethys", "Sedna",
        "Ceres", "Orcus", "Salacia", "Varda", "Dysnomia", "Varuna", "Ixion", "Chaos", "Pallas",
        "Vesta", "Enceladus", "Miranda", "Huya", "Hygiea", "Proteus", "Mimas", "Hiʻiaka", "Vanth",
        "Ilmarë", "Nereid", "Interamnia", "Europa", "Davida", "Sylvia", "Actaea", "Cybele", "Juno",
        "Hyperion", "Eunomia", "Camilla", "Euphrosyne", "Psyche", "Chariklo", "Sila", "Bamberga",
        "Patientia", "Chiron", "Thisbe", "Hektor", "Ceto", "Herculina", "Doris", "Nunam",
        "Eugenia", "Phoebe", "Amphitrite", "Bienor", "Deucalion", "Diotima", "Egeria", "Fortuna",
        "Aurora", "Iris", "Daphne", "Themis", "Alauda", "Larissa", "Ursula", "Hermione", "Palma",
        "Metis", "Nemesis", "Hebe", "Pholus", "Bertha", "Freia", "Elektra", "Rhadamanthus",
        "Janus", "Aletheia", "Galatea", "Teharonhiawako", "Typhon", "Lachesis", "Winchester",
        "Hilda", "Himalia", "Namaka", "Puck", "Aegle", "Germania", "Prokne", "Stereoskopia",
        "Amalthea", "Agamemnon", "Kalliope", "Borasisi", "Siegena", "Elpis", "Diomedes", "Gyptis",
        "Aspasia", "Dioretsa", "Ida", "Comas Solà", "Atlas", "Elatus", "Perdita", "Pan", "Linus",
        "Ananke", "Telesto", "Phobos", "Paaliaq", "Francisco", "Calypso", "Leda", "Ferdinand",
        "Margaret", "Medusa", "Romulus", "Ymir", "Trinculo", "Cupid", "Euler", "Adrastea",
        "Kiviuq", "Herschel", "Tarvos", "Masursky", "Comet Comas Solà", "Bestla", "Kerberos",
        "Petit-Prince", "Deimos", "Gaspra", "Ijiraq", "Halley's Comet", "Styx", "Mab", "Erriapus",
        "Misterrogers", "Callirrhoe", "Themisto", "Daphnis", "Remus", "Toutatis", "Šteins",
        "Phaethon", "Cruithne", "Annefrank", "Pallene", "Polydeuces",
        "Comet Churyumov–Gerasimenko", "Methone", "Magellan", "Braille", "Geographos", "Apollo",
        "Astronautica", "Dactyl", "Icarus", "Castalia", "Nyx", "Hypnos", "Aten", "Golevka",
        "Itokawa", "Aegaeon", "Nereus", "Apophis", "Duende"};
    private final static String[] nameSuffixes = new String[] {"", "", "", "", "", "", "", "App",
        "App", "App", "Application", "Tool", "Beta"};

    private final static String loremIpsum =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor "
            + "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis "
            + "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
            + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu "
            + "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt "
            + "in culpa qui officia deserunt mollit anim id est laborum.";

    private final static String[] imageCategories = new String[] {"abstract", "animals",
        "business", "cats", "city", "food", "nightlife", "fashion", "people", "nature", "sports",
        "technics", "transport"};
    private final static String[] images = new String[] {"http://i.imgur.com/GHqgU71.jpg",
        "http://i.imgur.com/2FdFykW.jpg", "http://i.imgur.com/HngIsi8.jpg",
        "http://i.imgur.com/PVq1E48.jpg", "http://i.imgur.com/JenkQ6W.jpg",
        "http://i.imgur.com/7G2JjkR.jpg", "http://i.imgur.com/Im5G6eQ.jpg",
        "http://i.imgur.com/oIBvEKj.jpg", "http://i.imgur.com/ia6PcPs.jpg",
        "http://i.imgur.com/foYukns.jpg", "http://i.imgur.com/RSxkEsH.jpg",
        "http://i.imgur.com/tqUfedw.jpg", "http://i.imgur.com/xMvMvqK.jpg",
        "http://i.imgur.com/3VYM1Gy.jpg", "http://i.imgur.com/e2Nt0jg.jpg",
        "http://i.imgur.com/SHgvm2d.jpg", "http://i.imgur.com/exv79Vi.jpg",
        "http://i.imgur.com/5KRULPO.jpg", "http://i.imgur.com/nAWDAft.jpg",
        "http://i.imgur.com/V5UlLVy.jpg", "http://i.imgur.com/BXOpcSz.jpg",
        "http://i.imgur.com/ApteZX1.jpg", "http://i.imgur.com/8wYq8x0.jpg",
        "http://i.imgur.com/0jT9sb1.jpg", "http://i.imgur.com/UB9dHqk.jpg",
        "http://i.imgur.com/NKawdIF.jpg", "http://i.imgur.com/HIxueyv.jpg",
        "http://i.imgur.com/gA02T4p.jpg", "http://i.imgur.com/3X5XEUb.jpg",
        "http://i.imgur.com/NDD3WlY.jpg", "http://i.imgur.com/Quazb9N.jpg",
        "http://i.imgur.com/DBDon2W.jpg", "http://i.imgur.com/Fd8qOOA.jpg",
        "http://i.imgur.com/GiUSHvX.jpg", "http://i.imgur.com/uM5grFZ.jpg",
        "http://i.imgur.com/zBBx7jP.jpg", "http://i.imgur.com/gaAqBso.jpg",
        "http://i.imgur.com/cJTetYa.jpg", "http://i.imgur.com/mCbUkO6.jpg",
        "http://i.imgur.com/yrxhYri.jpg", "http://i.imgur.com/CclmTGh.jpg",
        "http://i.imgur.com/AvW7ZGH.jpg", "http://i.imgur.com/ThZhD6E.jpg",
        "http://i.imgur.com/DuGHh5z.jpg", "http://i.imgur.com/UucIiQc.jpg",
        "http://i.imgur.com/Rx2YXQr.jpg", "http://i.imgur.com/xxAqUpz.jpg",
        "http://i.imgur.com/heSczUw.jpg"};
    private final static int IMAGE_W = 500;
    private final static int IMAGE_H = 280;
    private final static int LOGO_W = 150;
    private final static int LOGO_H = 150;

    /**
     * Generates a shuffled lorem ipsum textblock.
     * 
     * @return a shuffled and capitalized lorem ipsum text
     */
    public static String getRandomIpsumBlock() {
      String[] parts = loremIpsum.split(" ");
      shuffleArray(parts);
      String block = combine(parts, " ").toLowerCase();
      block = capitalize(block) + ".";
      return block;
    }

    /**
     * Capitalizes the first letter after a dot
     * 
     * @param input text to capitalize
     * @return capitalized text
     */
    private static String capitalize(String input) {
      StringBuilder out = new StringBuilder();
      if (input.length() < 0) {
        return "";
      }
      out.append(Character.toUpperCase(input.charAt(0)));
      int dotAndSpace = 0;
      for (int i = 1; i < input.length(); i++) {
        if (input.charAt(i) == '.') {
          dotAndSpace = 1;
          out.append(input.charAt(i));
        } else if (input.charAt(i) == ' ' && dotAndSpace == 1) {
          dotAndSpace = 2;
          out.append(input.charAt(i));
        } else {
          if (dotAndSpace == 2) {
            out.append(Character.toUpperCase(input.charAt(i)));
            dotAndSpace = 0;
          } else {
            out.append(input.charAt(i));
          }

        }
      }
      return out.toString();
    }

    /**
     * Joins elements of an array.
     * 
     * @param s array to join
     * @param glue what to put between the elements
     * @return single string with all elements of the array separated by the glue sequence
     */
    private static String combine(String[] s, String glue) {
      int k = s.length;
      if (k == 0) {
        return null;
      }
      StringBuilder out = new StringBuilder();
      out.append(s[0]);
      for (int x = 1; x < k; ++x) {
        out.append(glue).append(s[x]);
      }
      return out.toString();
    }

    /**
     * Fisher–Yates Shuffle
     * 
     * @param ar array to shuffle
     */
    private static void shuffleArray(String[] ar) {
      Random rnd = RandomNumberGenerator.getRandom();
      for (int i = ar.length - 1; i > 0; i--) {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        String a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }

    /**
     * Finds all occurrence of a substring in a string
     * 
     * @param input base string
     * @param subString substring to search for
     * @return list of indices found
     */
    private static ArrayList<Integer> findAll(String input, String subString) {
      int lastIndex = 0;
      ArrayList<Integer> indices = new ArrayList<Integer>();
      while (lastIndex != -1) {

        lastIndex = input.indexOf(subString, lastIndex);

        if (lastIndex != -1) {
          indices.add(lastIndex);
          lastIndex += subString.length();
        }
      }
      return indices;
    }

    /**
     * Inserts a text in a string at a specified index.
     * 
     * @param input base text to edit
     * @param index where to insert
     * @param toInsert what to insert
     * @return edited text
     */
    private static String insert(String input, int index, String toInsert) {
      return input.substring(0, index) + toInsert + input.substring(index);
    }

    /**
     * Generates a random name using two internal name arrays and combining random elements of
     * those.
     * 
     * @return a random name
     */
    public static String getRandomName() {
      String name =
          names[RandomNumberGenerator.getRandomInt(0, names.length - 1)] + " "
              + nameSuffixes[RandomNumberGenerator.getRandomInt(0, nameSuffixes.length - 1)];
      return name.trim();
    }

    /**
     * Generates a random lorem ipsum text.
     * 
     * @param wordCount amount of words required
     * @param paragraphs amount of empty lines to insert
     * @return random lorem ipsum text
     */
    public static String getRandomText(int wordCount, int paragraphs) {
      StringBuilder sb = new StringBuilder();
      int wordSize = 0;
      String[] parts = new String[] {};
      int i = 1000;

      while (wordSize < wordCount) {
        if (i >= parts.length) {
          i = 0;
          parts = getRandomIpsumBlock().split(" ");
        }
        sb.append(parts[i]);
        if (wordSize + 1 < wordCount) {
          sb.append(" ");
        } else {
          sb.append(".");
        }
        i++;
        wordSize++;
      }

      String text = sb.toString();
      ArrayList<Integer> dots = findAll(text, ".");

      /*
       * for (int j = 0; j < dots.size(); j++) { System.out.println(dots.get(j)); }
       */

      for (int j = 0; j < paragraphs - 1; j++) {
        if (dots.size() <= 2) {
          break;
        }

        int randomIndex = RandomNumberGenerator.getRandomInt(0, dots.size() - 1);
        Integer index = dots.get(randomIndex);
        text = insert(text, index + 2, "\n\n");

        for (int k = randomIndex + 1; k < dots.size(); k++) {
          dots.set(k, dots.get(k) + 2);
        }
        dots.remove(randomIndex);



      }

      return text;
    }

    /**
     * Generates a random image url for an app using lorempixum service.
     * 
     * @return an url to a random image
     */
    public static String getRandomImageUrl() {
      return "http://lorempixel.com/" + IMAGE_W + "/" + IMAGE_H + "/"
          + imageCategories[RandomNumberGenerator.getRandomInt(0, imageCategories.length - 1)];
    }

    /**
     * Generates a random image url for an app using lorempixel service.
     * 
     * @return an url to a random image
     */
    public static String getRandomLogoUrl() {
      return "http://lorempixel.com/" + LOGO_W + "/" + LOGO_H + "/"
          + imageCategories[RandomNumberGenerator.getRandomInt(0, imageCategories.length - 1)];

    }

    /**
     * Generates an array of random image urls
     * 
     * @param amount size of the array to generate
     * @return array with urls of random images
     */
    public static String[] getRandomImageUrls(int amount) {
      String[] res = new String[amount];
      for (int i = 0; i < amount; i++) {
        res[i] = getRandomImageUrl();
      }
      return res;
    }

    /**
     * Returns a tuple (thumbnail, logo) from the set {@link #images}.
     * 
     * @return
     */
    public static String[] getImagePair() {
      String[] pair = new String[2];
      int index = generateRandomInt(1, images.length - 1);
      if (index % 2 == 1) {
        pair[0] = images[index - 1];
        pair[1] = images[index];
      } else {
        pair[0] = images[index];
        pair[1] = images[index + 1];
      }
      return pair;
    }
  }

  /**
   * Static class to generate a fixed sequence of random numbers (for unit tests)
   */
  public static class RandomNumberGenerator {
    private static RandomNumberGenerator instance = null;
    private final static int SEED = 9001;
    private Random rand = new Random(SEED);

    protected RandomNumberGenerator() {

    }

    /**
     * Generates a random integer value in a range.
     * 
     * @param min lowest possible value
     * @param max highest possible value
     * @return random value in [min,max]
     */
    public static int getRandomInt(int min, int max) {
      return getInstance().rand.nextInt((max - min) + 1) + min;
    }

    public static Random getRandom() {
      return getInstance().rand;
    }

    /**
     * Retrieves the singleton instance.
     * 
     * @return singleton instance
     */
    public static RandomNumberGenerator getInstance() {
      if (instance == null) {
        instance = new RandomNumberGenerator();
      }
      return instance;
    }
  }
}
