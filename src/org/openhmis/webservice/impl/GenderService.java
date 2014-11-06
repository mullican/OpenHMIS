package org.openhmis.webservice.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.openhmis.domain.CodeGender;
import org.openhmis.exception.gender.GenderNotFoundException;
import org.openhmis.service.GenderManager;
import org.openhmis.service.impl.GenderManagerImpl;
import org.openhmis.vo.GenderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/genders")
public class GenderService 
{
	private static final Logger log = LoggerFactory.getLogger(GenderService.class);
	private GenderManager genderManager;
	Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
	public GenderService()
	{
		genderManager = new GenderManagerImpl();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<GenderVO> getAllGenders() throws GenderNotFoundException
	{
		log.debug("getAllGenders");
		List<GenderVO> genderVOList = null;
		try
		{
			genderVOList = new ArrayList<GenderVO>();
			List<CodeGender> genderList = genderManager.getGenders();
			if ((genderList == null) || (genderList.isEmpty()))
			{
				throw new GenderNotFoundException("No Gender Found");
			}
			log.debug("retrieve gender list" + genderList.toString());
			for (CodeGender g: genderList)
			{
				GenderVO genderVO = mapper.map(g, GenderVO.class);
				genderVOList.add(genderVO);
			}
		}
		catch(Exception e)
		{
			log.error("exception in get All Genders");
			throw new GenderNotFoundException(e.getMessage());
		}
		return genderVOList;
	}
	
	@Path("/addGender")
	@POST
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public GenderVO addGender(JAXBElement<GenderVO> gender)
	{
		log.debug("addGender");
		GenderVO genderVO = null;
		try
		{
			genderVO = gender.getValue();
			CodeGender newGender = mapper.map(genderVO, CodeGender.class);
			genderManager.addGender(newGender);
		}
		catch(Exception e)
		{
			log.error("Couldn't add the gender " + e.getMessage());
			e.printStackTrace();
		}
		return genderVO;
	}
	public GenderManager getGenderManager() {
		return genderManager;
	}
	public void setGenderManager(GenderManager genderManager) {
		this.genderManager = genderManager;
	}
}