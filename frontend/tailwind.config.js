/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#3490dc',
        'primary-dark': '#2779bd',
        teal: {
          800: '#285e61',
        },
        info: '#38bdf8'  // info color (light blue)
      }
    },
  },
  plugins: [],
}