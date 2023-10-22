import Link from 'next/link'

export default function Footer() {
  return (
    <footer>
      <div className="footer clearfix mb-0 text-muted">
        <div className="float-start">
          <span>2021 &copy; Level Uphaken</span> 
          &nbsp; &ndash; &nbsp; 
          <span>
            <Link href="/impress" >
              <a>Impressum</a>
            </Link>
          </span>
          &nbsp; &ndash; &nbsp; 
          <span>
            <Link href="/privacy" >
              <a>Datenschutzerklärung</a>
            </Link>
          </span>
        </div>
        <div className="float-end"> <p>Made with <span className="text-danger"><i className="bi bi-heart"></i></span> by Team Level Uphaken &mdash; Theme by <a href="http://ahmadsaugi.com">A. Saugi</a></p> </div>
      </div>
    </footer>
  );
}