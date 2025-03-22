// src/constants/menuItems.js

const MENU_ITEMS = [
  { 
    label: 'HOME', 
    subItems: [],
    hasDropdown: false
  },
    { 
      label: 'ABOUT', 
      subItems: ['Company', 'Team', 'Mission'],
      hasDropdown: true
    },
   
    { 
      label: 'MARKETS', 
      subItems: [],
      hasDropdown: false
    },
    { 
      label: 'NEWS', 
      subItems: ['Latest News', 'Press Releases', 'Blog'],
      hasDropdown: true
    },
    { 
      label: 'WEATHER', 
      subItems: [],
      hasDropdown: false
    }
  ];
  
  // App constants
  export const APP_CONSTANTS = {
    companyName: "AGRI TEST",
    companySubtitle: "Pro.",
    mainHeading: "Integrated Agricultural Product Testing and Management System",
  };
  
  export default MENU_ITEMS;