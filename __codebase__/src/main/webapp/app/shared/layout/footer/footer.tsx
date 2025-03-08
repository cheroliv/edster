import React from 'react';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="container">
        <div className="row">
          <div className="col-md-6">
            <p>&copy; {new Date().getFullYear()} cccp.education. All rights reserved.</p>
          </div>
          <div className="col-md-6 text-md-right">
            <ul className="list-inline">
              <li className="list-inline-item">
                <a style={{ color: 'white' }} href="https://cheroliv.github.io/blog.html" target="_blank" rel="noopener noreferrer">
                  Blog
                </a>
              </li>
              <li className="list-inline-item">
                <a style={{ color: 'white' }} href="https://cheroliv.github.io/about.html" target="_blank" rel="noopener noreferrer">
                  Documentation
                </a>
              </li>
              <li className="list-inline-item">
                <a style={{ color: 'white' }} href="https://github.com/cccp-education/" target="_blank" rel="noopener noreferrer">
                  GitHub
                </a>
              </li>
              <li className="list-inline-item">
                <a style={{ color: 'white' }} href="https://youtube.com/@cccp.education/" target="_blank" rel="noopener noreferrer">
                  YouTube
                </a>
              </li>
              <li className="list-inline-item">
                <a style={{ color: 'white' }} href="https://cheroliv.github.io/about.html" target="_blank" rel="noopener noreferrer">
                  About
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
