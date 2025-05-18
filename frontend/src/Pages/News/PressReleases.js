import React from 'react';
import Header from '../../Components/Header/Header';
import Footer from '../../Components/Footer/Footer';
import SearchBar from '../../Components/SearchBar/SearchBar';
import NewsCard from '../../Components/NewsCard/NewsCard';

const PressReleases = (props) => {
  // Sample press releases data
  const pressReleases = [
    {
      id: 1,
      title: "Agri Test Launches New Soil Analysis Service",
      date: "March 22, 2025",
      category: "Official Statement",
      content: "Agri Test is proud to announce the launch of our new comprehensive soil analysis service, designed to help farmers maximize their yield through data-driven insights.",
      link: "/press/soil-analysis-launch"
    },
    {
      id: 2,
      title: "Agri Test Partners with National Farmers Association",
      date: "March 10, 2025",
      category: "Partnership",
      content: "We're excited to announce our strategic partnership with the National Farmers Association, aimed at bringing advanced agricultural technology to small-scale farmers across the country.",
      link: "/press/national-farmers-association-partnership"
    },
    {
      id: 3,
      title: "Agri Test to Host Annual Agricultural Innovation Summit",
      date: "February 28, 2025",
      category: "Event",
      content: "Mark your calendars for the 5th Annual Agricultural Innovation Summit, to be held at the Grand Convention Center on April 15-17, 2025.",
      link: "/press/innovation-summit-announcement"
    },
    {
      id: 4,
      title: "Agri Test Expands Operations to Three New Regions",
      date: "February 15, 2025",
      category: "Expansion",
      content: "As part of our continued growth strategy, Agri Test is pleased to announce the expansion of our operations to three new agricultural regions.",
      link: "/press/regional-expansion"
    },
    {
      id: 5,
      title: "Agri Test Introduces Weather Forecasting Technology for Farmers",
      date: "January 30, 2025",
      category: "Product Launch",
      content: "Our new weather forecasting technology provides farmers with hyper-local, accurate predictions to help manage weather-related risks.",
      link: "/press/weather-tech-launch"
    },
    {
      id: 6,
      title: "Agri Test Reports Record Growth in Q4 2024",
      date: "January 15, 2025",
      category: "Financial",
      content: "Agri Test is pleased to report record growth for the fourth quarter of 2024, with a 35% increase in user base and 42% growth in revenue.",
      link: "/press/q4-growth"
    }
  ];

  // Function to get category badge color
  const getCategoryColor = (category) => {
    switch(category) {
      case 'Official Statement':
        return 'bg-green-100 text-green-800';
      case 'Partnership':
        return 'bg-blue-100 text-blue-800';
      case 'Event':
        return 'bg-purple-100 text-purple-800';
      case 'Expansion':
        return 'bg-orange-100 text-orange-800';
      case 'Product Launch':
        return 'bg-indigo-100 text-indigo-800';
      case 'Financial':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };
  
  return (
    <div>
      <Header {...props} />
      <main className="container mx-auto py-8 px-4">
        <h1 className="text-3xl font-bold mb-6">Press Releases</h1>
        
        <SearchBar />
        
        <div className="mb-8">
          <div className="flex flex-wrap gap-4 mb-6">
            <button className="bg-green-600 text-white px-4 py-2 rounded">All Releases</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Official Statements</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Partnerships</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Events</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Financial</button>
          </div>
        </div>
        
        <div className="space-y-6">
          {pressReleases.map(item => (
            <div key={item.id} className="bg-white rounded shadow p-6">
              <div className="flex items-center mb-4">
                <div className={`px-3 py-1 rounded-full text-sm font-medium ${getCategoryColor(item.category)}`}>
                  {item.category}
                </div>
                <span className="text-gray-500 ml-4">{item.date}</span>
              </div>
              <h2 className="text-2xl font-semibold mb-3">{item.title}</h2>
              <p className="text-gray-700 mb-4">
                {item.content}
              </p>
              <button className="text-blue-600 hover:underline">Read full press release</button>
            </div>
          ))}
        </div>
        
        <div className="mt-8 flex justify-center">
          <div className="flex gap-2">
            <button className="w-10 h-10 bg-green-600 text-white rounded flex items-center justify-center">1</button>
            <button className="w-10 h-10 bg-white border border-gray-300 rounded flex items-center justify-center">2</button>
            <button className="w-10 h-10 bg-white border border-gray-300 rounded flex items-center justify-center">3</button>
            <button className="w-10 h-10 bg-white border border-gray-300 rounded flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </button>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default PressReleases;