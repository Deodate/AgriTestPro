import React from 'react';
import Header from '../Components/Header/Header';
import Footer from '../Components/Footer/Footer';

const News = (props) => {
  // Sample news articles
  const featuredArticle = {
    title: "New Agricultural Policy Set to Transform Farming Practices",
    date: "March 25, 2025",
    author: "Jessica Thompson",
    category: "Policy",
    excerpt: "The government has announced a comprehensive new agricultural policy aimed at promoting sustainable farming practices while increasing productivity and farmer income.",
    imageUrl: "/images/policy-news.jpg"
  };

  const newsArticles = [
    {
      id: 1,
      title: "Breakthrough in Drought-Resistant Crop Technology",
      date: "March 24, 2025",
      category: "Technology",
      excerpt: "Scientists have developed a new genetic modification technique that could increase crop resistance to drought conditions by up to 40%.",
    },
    {
      id: 2,
      title: "Global Food Prices Stabilize After Months of Volatility",
      date: "March 22, 2025",
      category: "Markets",
      excerpt: "International food commodity prices have shown signs of stabilization following six months of fluctuations caused by supply chain disruptions.",
    },
    {
      id: 3,
      title: "Agri Test Launches New Soil Analysis Service",
      date: "March 21, 2025",
      category: "Company News",
      excerpt: "Our new comprehensive soil analysis service is designed to help farmers maximize their yield through data-driven insights.",
    },
    {
      id: 4,
      title: "Climate Change Impact on Agricultural Regions: New Study Released",
      date: "March 20, 2025",
      category: "Research",
      excerpt: "A groundbreaking study maps the projected impact of climate change on agricultural productivity across different regions over the next 30 years.",
    },
    {
      id: 5,
      title: "Farming Community Embraces Digital Transformation",
      date: "March 18, 2025",
      category: "Technology",
      excerpt: "Rural farming communities are increasingly adopting digital tools and platforms to improve efficiency and access wider markets.",
    },
    {
      id: 6,
      title: "Sustainable Agriculture Summit Announced for June",
      date: "March 16, 2025",
      category: "Events",
      excerpt: "The annual Sustainable Agriculture Summit will bring together industry leaders, researchers, and policymakers to discuss the future of farming.",
    },
  ];

  return (
    <div>
      <Header {...props} />
      <main className="container mx-auto py-8 px-4">
        <h1 className="text-3xl font-bold mb-6">Agricultural News</h1>
        
        {/* Featured Article */}
        <div className="bg-white rounded shadow overflow-hidden mb-8">
          <div className="md:flex">
            <div className="md:w-2/5 bg-gray-300 md:h-auto h-48 flex items-center justify-center">
              {/* This would be an actual image in production */}
              <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
            </div>
            <div className="md:w-3/5 p-6">
              <div className="flex items-center mb-2">
                <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded">
                  {featuredArticle.category}
                </span>
                <span className="text-gray-500 text-sm ml-3">{featuredArticle.date}</span>
              </div>
              <h2 className="text-2xl font-bold mb-3">{featuredArticle.title}</h2>
              <p className="text-gray-600 mb-4">By {featuredArticle.author}</p>
              <p className="text-gray-700 mb-4">{featuredArticle.excerpt}</p>
              <a href="#" className="text-blue-600 font-medium hover:underline">Read more</a>
            </div>
          </div>
        </div>
        
        {/* News Categories */}
        <div className="mb-8">
          <div className="flex flex-wrap gap-4 mb-6">
            <button className="bg-green-600 text-white px-4 py-2 rounded">All News</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Technology</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Policy</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Markets</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Research</button>
            <button className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded">Events</button>
          </div>
        </div>
        
        {/* Latest News Articles */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          {newsArticles.map(article => (
            <div key={article.id} className="bg-white rounded shadow overflow-hidden">
              <div className="p-6">
                <div className="flex items-center mb-2">
                  <span className="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded">
                    {article.category}
                  </span>
                  <span className="text-gray-500 text-sm ml-3">{article.date}</span>
                </div>
                <h3 className="text-xl font-semibold mb-2">{article.title}</h3>
                <p className="text-gray-700 mb-4">{article.excerpt}</p>
                <a href="#" className="text-blue-600 hover:underline">Continue reading</a>
              </div>
            </div>
          ))}
        </div>
        
        {/* Newsletter Signup */}
        <div className="bg-green-50 rounded shadow p-6">
          <div className="md:flex items-center">
            <div className="md:w-2/3 mb-4 md:mb-0 md:pr-6">
              <h2 className="text-xl font-semibold mb-2">Stay Updated with Agricultural News</h2>
              <p className="text-gray-700">Subscribe to our newsletter to receive the latest news, market updates, and research findings directly in your inbox.</p>
            </div>
            <div className="md:w-1/3">
              <div className="flex">
                <input type="email" className="flex-grow px-4 py-2 rounded-l border border-gray-300 focus:outline-none focus:border-green-500" placeholder="Your email address" />
                <button className="bg-green-600 text-white px-4 py-2 rounded-r hover:bg-green-700 transition-colors">Subscribe</button>
              </div>
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default News;