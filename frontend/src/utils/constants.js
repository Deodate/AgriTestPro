export const APP_CONSTANTS = {
  companyName: "AgriTestPro",
  companySubtitle: "Agricultural Testing Solutions",
  mainHeading: "Welcome to AgriTestPro",
  companyTagline: "Revolutionizing Agricultural Testing"
};

export const MENU_ITEMS = [
  {
    label: "Home",
    isHighlighted: true,
    hasDropdown: false
  },
  {
    label: "Services",
    isHighlighted: false,
    hasDropdown: true,
    subItems: ["Soil Testing", "Water Analysis", "Crop Quality"]
  },
  {
    label: "About",
    isHighlighted: false,
    hasDropdown: true,
    subItems: ["Our Story", "Team", "Partners"]
  },
  {
    label: "Resources",
    isHighlighted: false,
    hasDropdown: true,
    subItems: ["Documentation", "API", "Support"]
  }
]; 