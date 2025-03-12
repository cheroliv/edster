import React from 'react';

const Footer = () => {
  return (
    <footer className="footer py-2 mt-0">
      <div className="container-fluid">
        <div className="row mb-2">
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center flex-wrap pt-1">
              <a
                href="https://cheroliv.github.io/blog.html"
                className="footer-link mb-2 mb-lg-0 text-white"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'white' }}
              >
                <i className="bi bi-file-text-fill me-2 footer-icon text-white"></i>
                <span className="text-white">Blog</span>
              </a>

              <a
                href="https://cheroliv.github.io/about.html"
                className="footer-link mb-2 mb-lg-0 text-white"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'white' }}
              >
                <i className="bi bi-book-fill me-2 footer-icon text-white"></i>
                <span className="text-white">Documentation</span>
              </a>

              <a
                href="https://github.com/cccp-education/"
                className="footer-link mb-2 mb-lg-0 text-white"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'white' }}
              >
                <i className="bi bi-github me-2 footer-icon text-white"></i>
                <span className="text-white">GitHub</span>
              </a>

              <a
                href="https://youtube.com/@cccp.education/"
                className="footer-link mb-2 mb-lg-0 text-white"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'white' }}
              >
                <i className="bi bi-play-btn-fill me-2 footer-icon text-white"></i>
                <span className="text-white">YouTube</span>
              </a>

              <a href="mailto:contact@cccp.education" className="footer-link mb-2 mb-lg-0 text-white" style={{ color: 'white' }}>
                <i className="bi bi-envelope-fill me-2 footer-icon text-white"></i>
                <span className="text-white">Contact</span>
              </a>

              <a
                href="https://cheroliv.github.io/about.html"
                className="footer-link mb-2 mb-lg-0 text-white"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'white' }}
              >
                <i className="bi bi-question-circle-fill me-2 footer-icon text-white"></i>
                <span className="text-white">About</span>
              </a>
            </div>
          </div>
        </div>

        <hr className="footer-divider my-2" />

        <div className="row">
          <div className="col-12 text-center">
            <p className="mb-1 text-white">&copy; {new Date().getFullYear()} cccp.education. All rights reserved.</p>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
