import Head from 'next/head'
import Image from 'next/image'
import { getSession } from 'next-auth/client'
import { requestAPI } from '../../utils/request'

export default function Index({ profile }) {
  const progress = (profile.levelsystem.xp / (profile.levelsystem.xp + profile.levelsystem.missingXP)) * 100

  return (
    <>
      <Head>
        <title>Profile</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <div className="row">
            <div className="col mb-3">
              <h3>Profile</h3>
            </div>
          </div>
        </div>
        <section className="section row">
          <div className="col-3">
            <div className="card">
              <div className="card-body text-center">
                <h1>{profile.firstName} {profile.lastName} </h1>
                <Image src={`https://eu.ui-avatars.com/api/?name=${profile.firstName}+${profile.lastName}&size=512`} width='256px' height='256px' />
              </div>
            </div>
          </div>
          <div className="col-9">
            <div className="card">
              <div className="card-body">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse non quam mi. Vivamus et lorem nisi. Pellentesque tortor turpis, ornare id neque vitae, porta ultrices justo. Vestibulum in turpis quis quam facilisis vestibulum. Morbi volutpat felis sit amet justo sagittis, id ullamcorper sem tempus. Sed gravida arcu in luctus mattis. Aenean mattis consequat nibh, bibendum vulputate est tristique a. Etiam mattis sapien a dictum scelerisque.
                <br /><br />
                Mauris et libero a ipsum lobortis congue. Nam odio tortor, lobortis in tellus eu, vehicula laoreet ante. Ut euismod fringilla urna, quis eleifend nisi placerat hendrerit. Praesent nec dolor at velit consequat congue. Duis rutrum eget leo non congue. Suspendisse potenti. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus fringilla tincidunt nisi, eget rutrum nunc sodales id. Mauris non finibus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Aliquam interdum purus sit amet ligula mollis, eget cursus augue maximus. Ut quis ligula ut ante hendrerit dignissim sit amet eu ex. Sed ultrices, enim id imperdiet placerat, eros nisl interdum felis, sed suscipit neque libero sed velit. Nulla consectetur ligula sit amet molestie rhoncus. Mauris elit lacus, ultricies eget eleifend at, efficitur sit amet metus.
                <br /><br />
                Etiam sed facilisis massa, eget hendrerit eros. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vitae enim condimentum, finibus est eget, vulputate mauris. Morbi et nisl suscipit, accumsan risus sit amet, efficitur lorem. Sed scelerisque diam in massa imperdiet porttitor. Aliquam sodales lacinia eros, blandit rhoncus massa ullamcorper eu. Vivamus ac dolor id nibh dignissim commodo non quis dolor. Nam aliquam magna sit amet mi interdum, vel rutrum justo porta. Donec fermentum maximus eros, nec congue turpis auctor at.
                <br /><br />
                Pellentesque accumsan, diam vitae pulvinar ultrices, tortor nisl rhoncus metus, non vulputate turpis lorem vitae ligula. Quisque est felis, egestas ac sagittis eu, sodales nec justo. Vestibulum faucibus vehicula pretium. Sed id maximus nibh. Morbi eget est tortor. Mauris varius orci vel egestas finibus. Aenean vulputate quis enim non vestibulum. Ut metus metus, dignissim ac metus in, suscipit pulvinar est. Cras sollicitudin pretium odio, et lobortis leo porttitor vel. Vivamus volutpat ante auctor leo tincidunt, at consectetur tellus convallis. Etiam porta tellus nec fermentum bibendum. Donec ut hendrerit magna. Fusce vel sem nec augue dignissim iaculis. Quisque nec dictum lorem. Mauris placerat dolor eget diam porta faucibus. Phasellus in pharetra ligula.
                <br /><br />
                Ut ullamcorper convallis magna, eu gravida nibh ultricies non. Maecenas blandit leo congue, pharetra neque et, ornare arcu. Sed blandit turpis mollis laoreet placerat. Nunc sit amet massa diam. Phasellus rutrum blandit malesuada. Vestibulum ultrices risus neque, sed vestibulum neque porttitor in. Vivamus sagittis pharetra erat, in fermentum urna scelerisque ut. Integer sodales, ante ac varius iaculis, felis mauris auctor eros, nec rutrum neque risus ut felis. Curabitur odio ipsum, pharetra sed nulla quis, porta consequat elit.
                <br /><br />
                Vestibulum elementum purus vel imperdiet ornare. Aenean vel auctor ligula. Etiam velit risus, laoreet ac elementum eu, pretium sed massa. Praesent lacinia vitae leo at condimentum. Donec consequat massa ut ante suscipit rutrum eget ac ante. Sed bibendum nec ante et cursus. Praesent laoreet scelerisque dignissim. Cras faucibus nibh id quam venenatis tristique. Cras quis nisl porta, egestas arcu at, varius nulla. Pellentesque rutrum ac enim eget congue. Pellentesque a neque leo.
              </div>
            </div>
          </div>
        </section>
        {profile.levelsystem && (
          <section className="section row">
            <div className="offset-3 col-6">
              <div className="float-start">
                {profile.levelsystem.xp}/{profile.levelsystem.xp + profile.levelsystem.missingXP}
              </div>
              <div className="float-end">
                {profile.levelsystem.levelName}
              </div>
              <div className="clearfix"></div>
              <div className="progress progress-info mb-3">
                <div className="progress-bar" role="progressbar" style={{ width: `${progress}%` }} aria-valuenow={parseFloat(progress).toFixed(2)}  aria-valuemin="0" aria-valuemax="100" />
              </div>
            </div>
          </section>
        )}
      </div>
    </>
  )
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

  try {
    const profile = await requestAPI(`${process.env.API_ENDPOINT}/users/info`, session)
    return {
      props: { session, profile }
    }
  } catch (error) {
    return {
      redirect: {
        destination: '/auth/logout',
        permanent: false,
      }
    }
  }
}