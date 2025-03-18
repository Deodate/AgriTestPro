/** @type {import('tailwindcss').Config} */
export default {
    content: [
      "./index.html",
      "./src/**/*.{js,jsx,ts,tsx}",
    ],
    theme: {
      extend: {
        colors: {
          primary: {
            DEFAULT: '#008000',
            dark: '#006400',
          },
          secondary: '#4CAF50'
        }
      },
    },
    plugins: [],
  }