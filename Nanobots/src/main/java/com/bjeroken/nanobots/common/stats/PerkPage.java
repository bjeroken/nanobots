package com.bjeroken.nanobots.common.stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerkPage {
  private String name;
  private List<Perk> perksList;

  public PerkPage(String name, List<Perk> perks) {
    this.name = name;
    this.perksList = perks;
  }

  public PerkPage(String name) {
    this.name = name;
    this.perksList = new ArrayList<Perk>();
  }

  public PerkPage(String name, Map<String, Perk> perks) {
    this.name = name;
    this.perksList = new ArrayList<Perk>(perks.values());
  }

  public String getName() {
    return name;
  }

  public List<Perk> getPerks() {
    return perksList;
  }

  private static List<PerkPage> PerkPages = new ArrayList<PerkPage>();

  public static void registerPerkPage(PerkPage page) {
    if (getPerkPage(page.getName()) == null)
      PerkPages.add(page);
  }

  public static PerkPage getPerkPage(int index) {
    return PerkPages.get(index);
  }

  public static PerkPage getPerkPage(String name) {
    for (PerkPage page : PerkPages) {
      if (page.getName().equals(name)) {
        return page;
      }
    }
    return null;
  }

  public static Set<PerkPage> getPerkPages() {
    return new HashSet<PerkPage>(PerkPages);
  }

  public static boolean isPerkInPages(Perk Perk) {
    for (PerkPage page : PerkPages) {
      if (page.getPerks().contains(Perk)) {
        return true;
      }
    }
    return false;
  }

  public static String getTitle(int index) {
    return getPerkPage(index).getName();
  }

  public void add(Perk perk) {
    if (!this.perksList.contains(perk))
      this.perksList.add(perk);
  }
}