// src/constants/menuItems.js

const MENU_ITEMS = [
    { 
      label: 'ABOUT', 
      subItems: ['Company', 'Team', 'Mission'],
      hasDropdown: true
    },
    { 
      label: 'PRODUCTS', 
      subItems: [],
      hasDropdown: false
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
    },
    { 
      label: 'Signin', 
      subItems: [],
      hasDropdown: false,
      isHighlighted: true
    }
  ];
  
  // App constants
  export const APP_CONSTANTS = {
    companyName: "Rwanda Agriculture",
    companySubtitle: "Board",
    mainHeading: "Integrated Agricultural Product Testing and Management System",
    companyTagline: "AgriTest Pro"
  };
  
  export default MENU_ITEMS;