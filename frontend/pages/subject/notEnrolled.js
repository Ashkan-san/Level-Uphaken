import Link from "next/link";
import SingleCardPage from "../../components/singleCardPage";
import { getSession } from 'next-auth/client';

export default function NotEnrolled() {
  return <SingleCardPage title="Falsche Kursseite" headertext="Du bist nicht in diesem Kurs eingetragen">
    <Link href="/subject/">
      <a>
        <span>Hier </span>
      </a>
    </Link>

    findest Du die Kurs&uuml;bersicht.
  </SingleCardPage>
}

export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (!session) {
    return {
      redirect: {
        destination: '/auth/login',
        permanent: false,
      }
    }
  }

  return {
    props: {}
  }
}