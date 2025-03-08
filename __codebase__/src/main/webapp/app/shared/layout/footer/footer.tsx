import React from 'react';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="container">
        <div className="row">
          <div className="col-md-6">
            <p>&copy; {new Date().getFullYear()} Communication Tools Platform. All rights reserved.</p>
          </div>
          <div className="col-md-6 text-md-right">
            <ul className="list-inline">
              <li className="list-inline-item">
                <a href="https://github.com/your-github-username" target="_blank" rel="noopener noreferrer">
                  GitHub
                </a>
              </li>
              <li className="list-inline-item">
                <a href="https://youtube.com/your-youtube-channel" target="_blank" rel="noopener noreferrer">
                  YouTube
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
